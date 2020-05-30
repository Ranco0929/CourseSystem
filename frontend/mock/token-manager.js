const uuid = require('uuid').v4
const tokens = require('./db').tokens

const duration = 60 * 60 * 1000
const illegalTokenCode = 40002
const expiresTokenCode = 40003
const validTokenCode = 20000

function generateToken(email) {
  const token = uuid()
  const date = new Date().getTime()
  const expires = date + duration
  tokens[email] = {
    email,
    token,
    date,
    expires
  }
  return tokens[email]
}

function checkToken(token) {
  if(!token){
    return illegalTokenCode
  }
  token = JSON.parse(token)
  if(typeof token !== 'object' || !token.email || !token.token || !token.date || !token.expires){
    return illegalTokenCode
  }
  const validToken = tokens[token.email]
  // console.log('token-manager', validToken, token)
  if(validToken && validToken.token === token.token){
    if(validToken.date === token.date && validToken.expires === token.expires){
      if(new Date().getTime() < validToken.expires){
        return validTokenCode
      }else{
        delete tokens[token.email]
        return expiresTokenCode
      }
    }else{
      return illegalTokenCode
    }
  }else{
    return illegalTokenCode
  }
}

module.exports={
  generateToken,
  checkToken
}

