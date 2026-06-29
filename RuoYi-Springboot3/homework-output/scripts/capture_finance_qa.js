const fs = require('fs')
const path = require('path')
const { chromium } = require('playwright')

async function shot(page, outputDir, name) {
  await page.waitForLoadState('networkidle')
  await page.waitForFunction(() => {
    return !Array.from(document.querySelectorAll('.el-loading-mask')).some(element => {
      const style = window.getComputedStyle(element)
      return style.display !== 'none' && style.visibility !== 'hidden' && element.offsetParent !== null
    })
  }, null, { timeout: 10000 }).catch(() => {})
  await page.waitForTimeout(350)
  const file = path.join(outputDir, name)
  await page.screenshot({ path: file, fullPage: true })
  return file
}

async function openMenu(page, group, child) {
  const oldUrl = page.url()
  const childNode = page.getByText(child, { exact: true }).first()
  if (!(await childNode.isVisible().catch(() => false))) {
    await page.getByText(group, { exact: true }).first().click()
  }
  await childNode.click()
  await page.waitForFunction(previous => window.location.href !== previous, oldUrl, { timeout: 10000 })
  await page.waitForTimeout(700)
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
  await context.addCookies([{ name: 'Admin-Token', value: token, domain: 'localhost', path: '/' }])
  const page = await context.newPage()
  const consoleErrors = []
  page.on('console', message => {
    if (message.type() === 'error') consoleErrors.push(message.text())
  })
  page.on('pageerror', error => consoleErrors.push(error.message))

  const checks = {}
  const screenshots = []
  await page.goto('http://localhost:81/')

  const menuLabels = ['来访管理', '入退管理', '在住管理', '服务管理', '订单查询', '财务统计', '智能监测', '中州智询']
  const menuTops = []
  for (const label of menuLabels) {
    const locator = page.getByText(label, { exact: true }).first()
    menuTops.push(await locator.evaluate(node => node.getBoundingClientRect().top))
  }
  checks.menuOrder = menuTops.every((value, index) => index === 0 || value > menuTops[index - 1])

  const residenceTitle = page.getByText('在住管理', { exact: true }).first().locator('xpath=ancestor::*[contains(@class,\"el-submenu__title\")]')
  checks.residenceIcon = await residenceTitle.locator('.svg-icon').count() > 0

  await openMenu(page, '在住管理', '房型设置')
  screenshots.push(await shot(page, outputDir, 'defense-room-type-images.png'))
  const images = page.locator('.room-type-image img')
  checks.roomImages = await images.count() >= 3 && await images.evaluateAll(nodes =>
    nodes.every(node => node.complete && node.naturalWidth > 0)
  )

  await openMenu(page, '财务统计', '账单管理')
  screenshots.push(await shot(page, outputDir, 'defense-finance-bill.png'))
  const billText = await page.locator('body').innerText()
  checks.billPage = ['生成月度账单', '待支付', '已支付', '已关闭', '账单编号'].every(text => billText.includes(text))
  await page.getByRole('button', { name: '生成月度账单' }).click()
  checks.generateDialog = await page.getByRole('dialog', { name: '生成月度账单' })
    .getByText('账单月份', { exact: true }).isVisible()
  await page.keyboard.press('Escape')

  const payButton = page.locator('button:not(.is-disabled)').filter({ hasText: '支付' }).first()
  await payButton.click()
  checks.payDialog = await page.getByRole('dialog', { name: '上传支付凭证' })
    .getByText('支付凭证', { exact: true }).isVisible()
  await page.keyboard.press('Escape')

  await page.getByText('查看', { exact: true }).first().click()
  await page.waitForURL(/\/finance\/bill\/detail\/\d+/, { timeout: 10000 })
  await page.waitForTimeout(700)
  screenshots.push(await shot(page, outputDir, 'defense-finance-bill-detail.png'))
  const detailText = await page.locator('body').innerText()
  checks.billDetail = ['账单信息', '账单明细', '支付记录'].every(text => detailText.includes(text))

  await openMenu(page, '财务统计', '预缴款管理')
  screenshots.push(await shot(page, outputDir, 'defense-finance-prepayment.png'))
  await page.getByRole('button', { name: '充值' }).click()
  checks.rechargeDialog = await page.getByRole('dialog', { name: '上传充值凭证' })
    .getByText('充值凭证', { exact: true }).isVisible()
  await page.keyboard.press('Escape')

  await openMenu(page, '财务统计', '余额查询')
  screenshots.push(await shot(page, outputDir, 'defense-finance-balance.png'))
  checks.balancePage = await page.getByText('预缴款余额（元）', { exact: true }).isVisible()

  await openMenu(page, '财务统计', '欠费老人')
  screenshots.push(await shot(page, outputDir, 'defense-finance-arrears.png'))
  await page.getByText('查看', { exact: true }).first().click()
  checks.arrearsDialog = await page.getByText('查看欠费账单', { exact: true }).isVisible()
  screenshots.push(await shot(page, outputDir, 'defense-finance-arrears-dialog.png'))

  const report = { screenshots, checks, consoleErrors }
  fs.writeFileSync(path.join(outputDir, 'finance-qa.json'), JSON.stringify(report, null, 2), 'utf8')
  console.log(JSON.stringify(report, null, 2))
  await browser.close()
}

main().catch(error => {
  console.error(error)
  process.exit(1)
})
