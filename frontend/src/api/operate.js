import axios from 'axios'

const baseURL = '/api/operate'

export function getPosList() {
  return axios.get(`${baseURL}/poslist`)
}

export function getPolicyList() {
  return axios.get(`${baseURL}/pollist`)
}
