import request from '@/utils/request'

export function listOrders(query) {
  return request({
    url: '/nursing/order/list',
    method: 'get',
    params: query
  })
}

export function getOrder(id) {
  return request({
    url: `/nursing/order/${id}`,
    method: 'get'
  })
}

export function cancelOrder(id, reason) {
  return request({
    url: `/nursing/order/${id}/cancel`,
    method: 'put',
    data: { reason }
  })
}

export function applyRefund(id, reason) {
  return request({
    url: `/nursing/order/${id}/refund`,
    method: 'put',
    data: { reason }
  })
}

export function listRefunds(query) {
  return request({
    url: '/nursing/order/refund/list',
    method: 'get',
    params: query
  })
}

export function getRefund(id) {
  return request({
    url: `/nursing/order/refund/${id}`,
    method: 'get'
  })
}
