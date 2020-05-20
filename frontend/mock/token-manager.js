const uuid = require('uuid').v4

const duration = 60 * 60 * 1000
const illegalTokenCode = 40002
const expiresTokenCode = 40003
const validTokenCode = 20000

const tokens = {}

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
  token = JSON.parse(token)
  if(typeof token !== 'object' || !token.email || !token.token || !token.date || !token.expires){
    return illegalTokenCode
  }
  const validToken = tokens[token.email]
  if(validToken.token === token.token){
    if(validToken.date === token.date && validToken.expires === token.expires){
      if(new Date().getTime() < validToken.expires){
        return validTokenCode
      }else{
        delete validToken[email]
        return expiresTokenCode
      }
    }else{
      console.log("second")
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

