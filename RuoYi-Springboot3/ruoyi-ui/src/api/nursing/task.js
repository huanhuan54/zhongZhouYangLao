import request from '@/utils/request'

export function listTask(query) {
  return request({
    url: '/nursing/task/list',
    method: 'get',
    params: query
  })
}

export function getTask(id) {
  return request({
    url: '/nursing/task/' + id,
    method: 'get'
  })
}

export function executeTask(data) {
  return request({
    url: '/nursing/task/execute',
    method: 'put',
    data
  })
}

export function cancelTask(data) {
  return request({
    url: '/nursing/task/cancel',
    method: 'put',
    data
  })
}

export function rescheduleTask(data) {
  return request({
    url: '/nursing/task/reschedule',
    method: 'put',
    data
  })
}

export function changeTaskCaregiver(id, caregiverId) {
  return request({
    url: '/nursing/task/' + id + '/caregiver',
    method: 'put',
    data: { caregiverId }
  })
}
