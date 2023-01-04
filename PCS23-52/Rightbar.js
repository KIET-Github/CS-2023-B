import React, { useState, useEffect } from 'react'
import '../styles/rightbar.css'
import axios from 'axios'

const Rightbar = () => {

  const [recommendArray, setRecommendArray] = useState()

  useEffect(() => {
    const getRecommendedUsers = async () => {
      const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
      const obj = {userId: id}
      // console.log("scscsc", curr_user._id)
      const recommendedUsersArray = await axios.post("http://localhost:9002/api/getRecommendedFriends", obj)
      setRecommendArray(recommendedUsersArray.data.recommendedFriendListArray)
    }
    getRecommendedUsers()
  }, [])

  return (
    <div className='rightbar'>
        <div className='recommended-friends'>
          <div className='heading-1'>Recommended Friends</div>
            <div className='recommended-friends-container'>
              {
                (recommendArray && recommendArray.map((item) => {
                  return (
                    <div className='recommended-friend-container' key={item._id}>
                      <div className='recommended-friend-pic'>
                        <img src={item && "http://localhost:9002/images/" + item.profilePic}></img>
                      </div>
                      <div className='recommended-friend-username'>{item.username}</div>
                    </div>
                  )
                })) 
              }
            </div>
            {
              (recommendArray && recommendArray.length >= 4)
              ?
              <div className='button'>
                <button>Show More</button>
              </div>
              :
              null
            }
        </div>
        {console.log("mera", recommendArray)}
    </div>
  )
}

export default Rightbar