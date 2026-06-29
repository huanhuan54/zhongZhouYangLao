import request from '@/utils/request'

export function listProject(query) {
  return request({
    url: '/nursing/project/list',
    method: 'get',
    params: query
  })
}

export function getProject(id) {
  return request({
    url: '/nursing/project/' + id,
    method: 'get'
  })
}

export function addProject(data) {
  return request({
    url: '/nursing/project',
    method: 'post',
    data: data
  })
}

export function updateProject(data) {
  return request({
    url: '/nursing/project',
    method: 'put',
    data: data
  })
}

export function delProject(id) {
  return request({
    url: '/nursing/project/' + id,
    method: 'delete'
  })
}

export function getAllProjects() {
  return request({
    url: '/nursing/project/all',
    method: 'get'
  })
}
