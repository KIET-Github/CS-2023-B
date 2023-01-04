import React, { useContext, useEffect } from 'react'
import { Auth } from '../context/AuthContext'
import '../styles/profileCover.css'

const ProfileCover = () => {

  const {curr_user, setCurr_user} = useContext(Auth)

  useEffect(() => {
    const data = window.localStorage.getItem('Socially_Current_User')
    if(data !== null) setCurr_user(JSON.parse(data))
  }, [])

  return (
    <div className='profile-cover'>
      <div>
  
        <img src={curr_user && "http://localhost:9002/images/" + curr_user.coverPic}/>
      
      </div>
    </div>
  )
}

export default ProfileCover