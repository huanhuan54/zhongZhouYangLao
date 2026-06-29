export function parsePayload(value) {
  if (!value) return {}
  if (typeof value === 'object') return value
  try {
    return JSON.parse(value)
  } catch (e) {
    return {}
  }
}

export function serializePayload(value) {
  return JSON.stringify(value || {})
}

export function statusType(status) {
  if (['启用', '在线', '正常', '已入住', '生效中', '已签约', '已处理'].includes(status)) return 'success'
  if (['报警', '禁用', '已失效', '已退住', '退款失败'].includes(status)) return 'danger'
  if (['待处理', '未入住', '未生效'].includes(status)) return 'warning'
  return 'info'
}
