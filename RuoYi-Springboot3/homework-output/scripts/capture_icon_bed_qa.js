const fs = require('fs')
const path = require('path')
const { chromium } = require('playwright')

async function openMenu(page, group, child) {
  const childNode = page.getByText(child, { exact: true }).first()
  if (!(await childNode.isVisible().catch(() => false))) {
    await page.getByText(group, { exact: true }).first().click()
  }
  await childNode.click()
  await page.waitForTimeout(700)
}

async function iconName(locator) {
  return locator.locator('svg use').first().getAttribute('xlink:href')
}

async function waitForLoading(page) {
  await page.waitForFunction(() => {
    return !Array.from(document.querySelectorAll('.el-loading-mask')).some(element => {
      const style = window.getComputedStyle(element)
      return style.display !== 'none' && style.visibility !== 'hidden' && element.offsetParent !== null
    })
  }, null, { timeout: 10000 }).catch(() => {})
  await page.waitForTimeout(250)
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
  await page.goto('http://localhost:81/', { waitUntil: 'networkidle' })

  const responsible = page.getByText('负责老人', { exact: true }).first().locator('xpath=ancestor::li[1]')
  checks.responsibleIcon = (await iconName(responsible)) === '#icon-caregiver'
  const serviceMenu = page.getByText('服务管理', { exact: true }).first()
  if (!(await page.getByText('负责老人', { exact: true }).first().isVisible())) {
    await serviceMenu.click()
  }
  await page.getByText('负责老人', { exact: true }).first().scrollIntoViewIfNeeded()
  const responsibleScreenshot = path.join(outputDir, 'defense-responsible-icon-fixed.png')
  await page.screenshot({ path: responsibleScreenshot, fullPage: false })
  screenshots.push(responsibleScreenshot)

  await page.getByText('智能监测', { exact: true }).first().click()
  const device = page.getByText('设备管理', { exact: true }).first().locator('xpath=ancestor::li[1]')
  const alarmRule = page.getByText('报警规则', { exact: true }).first().locator('xpath=ancestor::li[1]')
  const alarmData = page.getByText('报警数据', { exact: true }).first().locator('xpath=ancestor::li[1]')
  checks.deviceIcon = (await iconName(device)) === '#icon-devices'
  checks.alarmRuleIcon = (await iconName(alarmRule)) === '#icon-alarm-light'
  checks.alarmDataIcon = (await iconName(alarmData)) === '#icon-alarm-record'
  await page.getByText('报警数据', { exact: true }).first().scrollIntoViewIfNeeded()
  const menuScreenshot = path.join(outputDir, 'defense-menu-icons-fixed.png')
  await page.screenshot({ path: menuScreenshot, fullPage: false })
  screenshots.push(menuScreenshot)

  await openMenu(page, '在住管理', '床位预览')
  await waitForLoading(page)
  checks.legendIcons = await page.locator('.bed-legend svg use').evaluateAll(nodes =>
    nodes.length === 3 && nodes.every(node => {
      const href = node.getAttribute('xlink:href') || node.getAttribute('href') || ''
      return href.endsWith('#icon-bed-status')
    })
  )
  const legendScreenshot = path.join(outputDir, 'defense-bed-legend-fixed.png')
  await page.screenshot({ path: legendScreenshot, fullPage: false })
  screenshots.push(legendScreenshot)

  const room101 = page.locator('.room-panel').filter({ hasText: '房间号：101' }).first()
  await room101.getByText('编辑', { exact: true }).click()
  const dialog = page.getByRole('dialog', { name: '编辑房间' })
  await dialog.waitFor()
  await page.waitForTimeout(500)
  checks.statusControls = await dialog.locator('.bed-status-row .el-select').count() === 3
  const dialogScreenshot = path.join(outputDir, 'defense-bed-status-edit.png')
  await page.screenshot({ path: dialogScreenshot, fullPage: true })
  screenshots.push(dialogScreenshot)

  const firstStatus = dialog.locator('.bed-status-row').first().locator('.el-select')
  await firstStatus.click()
  const visibleOptions = page.locator('.el-select-dropdown__item:visible')
  checks.statusLabels = await page.locator('.bed-page').evaluate(node => {
    const options = node.__vue__ && node.__vue__.statusOptions
    return Array.isArray(options) &&
      ['空闲', '已入住', '请假中'].every(label => options.some(option => option.label === label))
  })
  await visibleOptions.filter({ hasText: '请假中' }).click()
  await dialog.getByRole('button', { name: '确定' }).click()
  await page.waitForTimeout(900)
  checks.persistedPending = await room101.locator('.bed-item').first().locator('.bed-icon.pending').count() === 1

  await room101.getByText('编辑', { exact: true }).click()
  const restoreDialog = page.getByRole('dialog', { name: '编辑房间' })
  await restoreDialog.locator('.bed-status-row').first().locator('.el-select').click()
  await page.locator('.el-select-dropdown__item:visible').filter({ hasText: '已入住' }).click()
  await restoreDialog.getByRole('button', { name: '确定' }).click()
  await page.waitForTimeout(900)
  checks.restoredOriginal = await room101.locator('.bed-item').first().locator('.bed-icon.occupied').count() === 1

  const result = { screenshots, checks, consoleErrors }
  fs.writeFileSync(
    path.join(outputDir, 'icon-bed-qa.json'),
    JSON.stringify(result, null, 2),
    'utf8'
  )
  await browser.close()
  console.log(JSON.stringify(result, null, 2))

  if (Object.values(checks).some(value => !value) || consoleErrors.length) {
    process.exitCode = 1
  }
}

main().catch(error => {
  console.error(error)
  process.exit(1)
})
