import axios from 'axios'
import React, { useContext, useEffect, useState } from 'react'
import { Auth } from '../context/AuthContext'
import '../styles/sidebar.css'

const Sidebar = () => {

  // const {curr_user, setCurr_user} = useContext(Auth)
  const [requestArray, setRequestArray] = useState([])
  const [friendsArray, setFriendsArray] = useState([])

  // useEffect(() => {
  //   const data = window.localStorage.getItem('Socially_Current_User')
  //   if(data !== null) setCurr_user(JSON.parse(data))
  //   console.log('1')
  // }, [])

  useEffect(() => {
    const getRequestUsers = async () => {
      const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
      const obj = {id: id}
      // console.log("scscsc", curr_user._id)
      const requestUserArray = await axios.post("http://localhost:9002/api/getRequestUser", obj)
      setRequestArray(requestUserArray.data.requestUsers)
    }
    getRequestUsers()
    console.log('2')
  }, [])

  useEffect(() => {
    const getFriends = async () => {
      const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
      const obj = {id: id}
      const getFriendsArray = await axios.post("http://localhost:9002/api/getFriends", obj)
      setFriendsArray(getFriendsArray.data.friends)
    }
    getFriends()
    // console.log("ghusa")
    console.log('3')
  }, [])

  const handleAcceptClick = async (requestUser) => {
      // now we will handle the database updation part

      const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
      

      const obj = {
        currUserId: id,
        requestUserId: requestUser._id
      }

      await axios.post("http://localhost:9002/api/friendArrayUpdate/currentUserUpdate/1", obj)
      .then(res => console.log(res))
      .catch(err => console.log(err))

      // console.log('1')

      await axios.post("http://localhost:9002/api/friendArrayUpdate/currentUserUpdate/2", obj)
      .then(res => console.log(res))
      .catch(err => console.log(err))

      // console.log('2')

      await axios.post("http://localhost:9002/api/friendArrayUpdate/requestUserUpdate", obj)
      .then(res => console.log(res))
      .catch(err => console.log(err))

      // recommend update

    
    
      // console.log('3')

      let tempArray = requestArray
      tempArray = tempArray.filter(function(obj) {
        return obj._id !== requestUser._id
      })
      setRequestArray(tempArray)

      // console.log('4')

      tempArray = friendsArray
      tempArray.push(requestUser)
      // console.log(tempArray)
      setFriendsArray(tempArray)
      // console.log('5')

      // window.location.reload()
  }

  return (
    <div className='sidebar'>
        <div className='friends'>
          <div className='heading-1'>Your Friends</div>
          {
              (friendsArray && friendsArray.map((item) => {
                return (
                  <div className='friends-item-container' key={item._id}>
                    <div className='img'>
                      <img src={item && "http://localhost:9002/images/" + item.profilePic}></img>
                    </div>
                    <div className='span'><div>{item && item.username}</div></div>

                  </div>
                )
              }))
          }
        </div>
        <div className='friend-request'>
          <div className='heading-2'>Friend-Request</div>
          {
                (requestArray && requestArray.map((item) => {
                  return ( 
                    <div className='request-item-container' key={item._id}>
                      <div className='img'>
                        <img src={item && "http://localhost:9002/images/" + item.profilePic}></img>
                      </div>
                      <div className='span'><div>{item && item.username}</div></div>
                      <div className='button'><button onClick={() => handleAcceptClick(item)}>accept</button></div>
                    </div> 
                )
              }))
          }
        </div>
    </div>
  )
}

export default Sidebar