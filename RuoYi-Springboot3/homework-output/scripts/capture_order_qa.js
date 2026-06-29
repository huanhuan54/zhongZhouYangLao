const fs = require('fs')
const path = require('path')
const { chromium } = require('playwright')

async function shot(page, outputDir, name) {
  await page.waitForLoadState('networkidle')
  await page.waitForTimeout(800)
  const filePath = path.join(outputDir, name)
  await page.screenshot({ path: filePath, fullPage: true })
  return filePath
}

async function main() {
  const root = process.cwd()
  const token = fs.readFileSync(
    path.resolve(root, '..', '..', 'homework-output', 'qa-token.txt'),
    'utf8'
  ).trim()
  const outputDir = path.join(root, 'homework-output', 'screenshots')
  fs.mkdirSync(outputDir, { recursive: true })

  const browser = await chromium.launch({
    headless: true,
    executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
  })
  const context = await browser.newContext({ viewport: { width: 1440, height: 900 } })
  await context.addCookies([{
    name: 'Admin-Token',
    value: token,
    domain: 'localhost',
    path: '/'
  }])
  const page = await context.newPage()
  const consoleErrors = []
  page.on('console', message => {
    if (message.type() === 'error') consoleErrors.push(message.text())
  })
  page.on('pageerror', error => consoleErrors.push(error.message))

  const screenshots = []
  await page.goto('http://localhost:81/')
  await page.getByText('订单查询', { exact: true }).click()
  await page.getByText('订单管理', { exact: true }).click()
  screenshots.push(await shot(page, outputDir, 'defense-order-list.png'))
  const listText = await page.locator('body').innerText()

  await page.locator('button:not(.is-disabled)').filter({ hasText: '取消' }).first().click()
  const cancelDialog = await page.getByText('填写取消原因', { exact: true }).isVisible()
  await page.keyboard.press('Escape')

  await page.locator('button:not(.is-disabled)').filter({ hasText: '退款' }).first().click()
  const refundDialog = await page.getByText('填写退款原因', { exact: true }).isVisible()
  await page.keyboard.press('Escape')

  await page.locator('.status-tabs label').filter({ hasText: '已执行' }).click()
  screenshots.push(await shot(page, outputDir, 'defense-order-executed.png'))

  await page.locator('tr').filter({ hasText: 'DD202606240003' }).getByText('查看', { exact: true }).click()
  screenshots.push(await shot(page, outputDir, 'defense-order-detail.png'))
  const detailText = await page.locator('body').innerText()

  await page.getByText('退款管理', { exact: true }).click()
  screenshots.push(await shot(page, outputDir, 'defense-refund-list.png'))
  const refundText = await page.locator('body').innerText()
  await page.getByText('查看', { exact: true }).first().click()
  await page.waitForTimeout(500)
  const refundDetail = await page.getByText('查看退款记录', { exact: true }).isVisible()
  screenshots.push(await shot(page, outputDir, 'defense-refund-detail.png'))

  const report = {
    screenshots,
    checks: {
      orderMenu: listText.includes('订单查询') && listText.includes('订单管理'),
      orderTabs: ['全部', '待支付', '待执行', '已执行', '已完成', '已退款', '已关闭']
        .every(text => listText.includes(text)),
      orderActions: ['取消', '退款', '查看'].every(text => listText.includes(text)),
      cancelDialog,
      refundDialog,
      detailSections: ['订单进度', '订单信息', '支付记录', '执行记录']
        .every(text => detailText.includes(text)),
      refundTabs: ['全部', '退款处理中', '退款成功', '退款失败']
        .every(text => refundText.includes(text)),
      refundDetail
    },
    consoleErrors
  }
  fs.writeFileSync(
    path.join(outputDir, 'order-qa.json'),
    JSON.stringify(report, null, 2),
    'utf8'
  )
  console.log(JSON.stringify(report, null, 2))
  await browser.close()
}

main().catch(error => {
  console.error(error)
  process.exit(1)
})
