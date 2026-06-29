import request from '@/utils/request'

export function sendChat(prompt) {
  return request({
    url: '/aiconsult/virtualagent/chat',
    method: 'post',
    data: { prompt },
    headers: { repeatSubmit: false },
    timeout: 50000
  })
}
