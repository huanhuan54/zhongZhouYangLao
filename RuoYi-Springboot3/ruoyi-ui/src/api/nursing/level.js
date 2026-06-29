import request from '@/utils/request'

export function listLevel(query) {
  return request({
    url: '/nursing/level/list',
    method: 'get',
    params: query
  })
}

export function getLevel(id) {
  return request({
    url: '/nursing/level/' + id,
    method: 'get'
  })
}

export function addLevel(data) {
  return request({
    url: '/nursing/level',
    method: 'post',
    data: data
  })
}

export function updateLevel(data) {
  return request({
    url: '/nursing/level',
    method: 'put',
    data: data
  })
}

export function delLevel(id) {
  return request({
    url: '/nursing/level/' + id,
    method: 'delete'
  })
}

export function listAllLevel() {
  return request({
    url: '/nursing/level/listAll',
    method: 'get'
  })
}
