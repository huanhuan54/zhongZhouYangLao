const fs = require('fs')
const path = require('path')
const { chromium } = require('playwright')

async function shot(page, outputDir, name) {
  await page.waitForLoadState('networkidle')
  await page.waitForTimeout(700)
  const file = path.join(outputDir, name)
  await page.screenshot({ path: file, fullPage: true })
  return file
}

async function bodyIncludes(page, texts) {
  const body = await page.locator('body').innerText()
  return texts.every(text => body.includes(text))
}

async function openMenu(page, group, child) {
  const groupNode = page.getByText(group, { exact: true }).first()
  const childNode = page.getByText(child, { exact: true }).first()
  if (!(await childNode.isVisible().catch(() => false))) {
    await groupNode.click()
  }
  await childNode.click()
  await page.waitForLoadState('networkidle')
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
  const checks = {}
  await page.goto('http://localhost:81/')

  await openMenu(page, '入退管理', '健康评估')
  screenshots.push(await shot(page, outputDir, 'defense-health-list.png'))
  checks.healthList = await bodyIncludes(page, ['健康评估', '上传体检报告'])
  await page.getByText('查看', { exact: true }).first().click()
  screenshots.push(await shot(page, outputDir, 'defense-health-detail.png'))
  checks.healthDetail = await bodyIncludes(page, ['体检总结', '疾病风险', '异常分析'])
  await page.getByRole('button', { name: '返回' }).click()

  await openMenu(page, '入退管理', '入住办理')
  screenshots.push(await shot(page, outputDir, 'defense-admission-list.png'))
  checks.admissionList = await bodyIncludes(page, ['入住办理', '发起入住申请'])
  await page.getByText('发起入住申请', { exact: true }).click()
  screenshots.push(await shot(page, outputDir, 'defense-admission-form.png'))
  checks.admissionForm = await bodyIncludes(page, ['基本信息', '家属信息', '入住配置', '签约办理', '账单预览'])
  await page.getByRole('button', { name: '账单预览' }).click()
  checks.admissionBill = await page.getByText('应收合计', { exact: true }).isVisible()
  await page.keyboard.press('Escape')
  await page.getByRole('button', { name: '返回' }).click()

  await openMenu(page, '入退管理', '退住办理')
  screenshots.push(await shot(page, outputDir, 'defense-checkout-list.png'))
  checks.checkoutList = await bodyIncludes(page, ['退住办理', '发起退住申请', '查看'])

  await openMenu(page, '在住管理', '房型设置')
  screenshots.push(await shot(page, outputDir, 'defense-room-type.png'))
  checks.roomType = await bodyIncludes(page, ['房型设置', '新增房型'])

  await openMenu(page, '在住管理', '床位预览')
  screenshots.push(await shot(page, outputDir, 'defense-bed-preview.png'))
  checks.bedPreview = await bodyIncludes(page, ['床位预览', '新增床位'])

  await openMenu(page, '在住管理', '智能床位')
  screenshots.push(await shot(page, outputDir, 'defense-smart-bed.png'))
  checks.smartBed = await bodyIncludes(page, ['智能床位', '报警状态'])

  await openMenu(page, '在住管理', '合同跟踪')
  screenshots.push(await shot(page, outputDir, 'defense-contract.png'))
  checks.contract = await bodyIncludes(page, ['合同跟踪', '合同状态'])

  await openMenu(page, '在住管理', '客户管理')
  screenshots.push(await shot(page, outputDir, 'defense-customer.png'))
  checks.customer = await bodyIncludes(page, ['客户管理', '客户昵称'])

  await openMenu(page, '智能监测', '设备管理')
  screenshots.push(await shot(page, outputDir, 'defense-device.png'))
  checks.deviceActions = await bodyIncludes(page, ['同步数据', '新增设备'])
  await page.getByText('查看', { exact: true }).first().click()
  screenshots.push(await shot(page, outputDir, 'defense-device-detail.png'))
  checks.deviceDetail = await bodyIncludes(page, ['设备信息', '设备详情', '物模型数据'])
  await page.getByRole('button', { name: '返回' }).click()

  await openMenu(page, '智能监测', '报警规则')
  screenshots.push(await shot(page, outputDir, 'defense-alarm-rule.png'))
  checks.alarmRule = await bodyIncludes(page, ['报警规则', '新增报警规则'])

  await openMenu(page, '智能监测', '报警数据')
  screenshots.push(await shot(page, outputDir, 'defense-alarm-data.png'))
  await page.getByText('处理', { exact: true }).first().click()
  checks.alarmDialog = await page
    .getByText('填写处理结果', { exact: true })
    .isVisible()
    .catch(() => false)
  await page.keyboard.press('Escape')

  const report = { screenshots, checks, consoleErrors }
  fs.writeFileSync(
    path.join(outputDir, 'operations-qa.json'),
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
