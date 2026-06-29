import request from '@/utils/request'

export function listResponsible(query) {
  return request({
    url: '/nursing/responsible/list',
    method: 'get',
    params: query
  })
}

export function listCaregivers() {
  return request({
    url: '/nursing/responsible/caregivers',
    method: 'get'
  })
}

export function assignCaregivers(data) {
  return request({
    url: '/nursing/responsible/assign',
    method: 'post',
    data
  })
}

export function getDashboardSummary() {
  return request({
    url: '/nursing/responsible/dashboard',
    method: 'get'
  })
}
