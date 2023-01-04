import axios from 'axios'
import React, { useContext } from 'react'
import { Auth } from '../context/AuthContext'
import '../styles/singleSearchItem.css'

const SingleSearchItem = ({item}) => {

    const {curr_user, setCurr_user} = useContext(Auth)

    const handleConnectClick = () => {
        const obj = {
            user_id: curr_user,
            friend_id: item._id,
            requests: item.requests
        }

        console.log(obj)

        axios.post("http://localhost:9002/api/connectUpdate", obj)
        .then(res => alert(res.data.message))
        .catch(err => console.log(err))
    }

    return (
      <div className='single-search-item'>

          <div className='img'>
              <img src={ "http://localhost:9002/images/" + item.profilePic}></img>
          </div>
          <div className='username'><span>{item.username} <span>(have {(item.friends.length >= 5) ? '5+' : item.friends.length} friends)</span></span></div>
          <div className='button' onClick={handleConnectClick}><button>+connect</button></div>

      </div>
    )
}

export default SingleSearchItem