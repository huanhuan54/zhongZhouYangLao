const fs = require('fs')
const path = require('path')
const { chromium } = require('playwright')

async function main() {
  const root = 'C:\\Users\\青峰不语\\Desktop\\黑马实训\\zzyl-springboot\\RuoYi-Springboot3'
  const token = fs.readFileSync('C:\\Users\\青峰不语\\Desktop\\黑马实训\\qa-token.txt', 'utf8').trim()
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

  const routes = [
    ['index', 'defense-home-light.png'],
    ['visit/appointment', 'defense-appointment-light.png'],
    ['visit/record', 'defense-visit-record-light.png'],
    ['nursing/project', 'defense-project-light.png'],
    ['nursing/responsible', 'defense-responsible-light.png'],
    ['aiconsult/virtualagent', 'defense-ai-light.png']
  ]

  const results = []
  for (const [route, fileName] of routes) {
    await page.goto(`http://localhost:81/${route}`, { waitUntil: 'networkidle' })
    await page.waitForTimeout(1200)
    const filePath = path.join(outputDir, fileName)
    await page.screenshot({ path: filePath, fullPage: true })
    results.push({
      route,
      title: await page.title(),
      screenshot: filePath,
      bodyText: (await page.locator('body').innerText()).slice(0, 1000)
    })
  }

  await page.goto('http://localhost:81/index', { waitUntil: 'networkidle' })
  const menuText = await page.locator('.sidebar-container').innerText()
  const theme = await page.locator('.sidebar-container').evaluate(el => ({
    background: getComputedStyle(el).backgroundColor,
    width: getComputedStyle(el).width
  }))

  const report = {
    menuText,
    theme,
    forbiddenMenus: ['系统管理', '系统监控', '系统工具', '若依官网'].filter(text => menuText.includes(text)),
    requiredMenus: ['首页', '来访管理', '服务管理', '中州智询'].filter(text => menuText.includes(text)),
    consoleErrors,
    results
  }
  fs.writeFileSync(path.join(outputDir, 'style-qa.json'), JSON.stringify(report, null, 2), 'utf8')
  console.log(JSON.stringify(report, null, 2))
  await browser.close()
}

main().catch(error => {
  console.error(error)
  process.exit(1)
})
