const fs = require('fs')
const path = require('path')
const { chromium } = require('playwright')

async function capture(page, outputDir, fileName) {
  await page.waitForLoadState('networkidle')
  await page.waitForTimeout(1000)
  const filePath = path.join(outputDir, fileName)
  await page.screenshot({ path: filePath, fullPage: true })
  return filePath
}

async function main() {
  const root = process.cwd()
  const tokenPath = path.resolve(root, '..', '..', 'homework-output', 'qa-token.txt')
  const token = fs.readFileSync(tokenPath, 'utf8').trim()
  const outputDir = path.join(root, 'homework-output', 'screenshots')
  fs.mkdirSync(outputDir, { recursive: true })

  const browser = await chromium.launch({
    headless: true,
    executablePath: 'C:\\Program Files\\Google\\Chrome\\Application\\chrome.exe'
  })
  const context = await browser.newContext({
    viewport: { width: 1440, height: 900 },
    deviceScaleFactor: 1
  })
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
  await page.goto('http://localhost:81/user/profile')
  screenshots.push(await capture(page, outputDir, 'defense-profile.png'))
  const profileText = await page.locator('body').innerText()

  await page.goto('http://localhost:81/nursing/task')
  screenshots.push(await capture(page, outputDir, 'defense-task-pending.png'))
  const pendingText = await page.locator('body').innerText()
  const dialogChecks = {}

  for (const [action, expectedText] of [
    ['执行', '新增执行记录'],
    ['取消', '填写取消原因'],
    ['改期', '修改执行时间'],
    ['换人', '更换护理员']
  ]) {
    await page.getByText(action, { exact: true }).first().click()
    await page.waitForTimeout(300)
    dialogChecks[action] = await page.getByText(expectedText, { exact: true }).isVisible()
    await page.keyboard.press('Escape')
  }

  await page.locator('.status-tabs label').filter({ hasText: '已执行' }).click()
  screenshots.push(await capture(page, outputDir, 'defense-task-executed.png'))

  await page.locator('.status-tabs label').filter({ hasText: '已取消' }).click()
  screenshots.push(await capture(page, outputDir, 'defense-task-cancelled.png'))

  await page.goto('http://localhost:81/nursing/task/detail/6')
  screenshots.push(await capture(page, outputDir, 'defense-task-detail.png'))
  const detailText = await page.locator('body').innerText()

  const report = {
    screenshots,
    profileChecks: {
      hasTitle: profileText.includes('个人信息'),
      hasName: profileText.includes('余佳俊'),
      hasPhone: profileText.includes('13878898898'),
      hasPasswordTab: profileText.includes('修改密码')
    },
    taskChecks: {
      hasTitle: pendingText.includes('任务安排'),
      hasPendingActions: ['取消', '查看', '执行', '改期'].every(text => pendingText.includes(text)),
      hasDetailSections: ['基本信息', '护理项目', '执行记录'].every(text => detailText.includes(text)),
      dialogChecks
    },
    consoleErrors
  }
  fs.writeFileSync(
    path.join(outputDir, 'profile-task-qa.json'),
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
