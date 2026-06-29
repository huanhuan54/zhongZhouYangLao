import request from '@/utils/request'

export function listPlan(query) {
  return request({
    url: '/nursing/plan/list',
    method: 'get',
    params: query
  })
}

export function getPlan(id) {
  return request({
    url: '/nursing/plan/' + id,
    method: 'get'
  })
}

export function addPlan(data) {
  return request({
    url: '/nursing/plan',
    method: 'post',
    data: data
  })
}

export function updatePlan(data) {
  return request({
    url: '/nursing/plan',
    method: 'put',
    data: data
  })
}

export function delPlan(id) {
  return request({
    url: '/nursing/plan/' + id,
    method: 'delete'
  })
}

export function getAllPlans() {
  return request({
    url: '/nursing/plan/all',
    method: 'get'
  })
}
