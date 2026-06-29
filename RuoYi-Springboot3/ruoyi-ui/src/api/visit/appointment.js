import request from '@/utils/request'

export function listAppointments(query) {
  return request({
    url: '/visit/appointment/list',
    method: 'get',
    params: query
  })
}

export function addAppointment(data) {
  return request({
    url: '/visit/appointment',
    method: 'post',
    data
  })
}

export function arriveAppointment(id, arrivalTime) {
  return request({
    url: '/visit/appointment/' + id + '/arrive',
    method: 'put',
    params: { arrivalTime }
  })
}

export function cancelAppointment(id) {
  return request({
    url: '/visit/appointment/' + id + '/cancel',
    method: 'put'
  })
}
