import axios from 'axios'
import React, { useContext, useEffect, useState } from 'react'
import { Auth } from '../context/AuthContext'
import '../styles/singlePost.css'
import FavoriteIcon from '@mui/icons-material/Favorite';
// import AccessAlarmIcon from '@mui/icons-material/AccessAlarm';
// import { AccessAlarm, ThreeDRotation } from '@mui/icons-material';

const SinglePost = ({post}) => {

  const [user, setUser] = useState()
  const [likeClick, setLikeClick] = useState()
  const [likeArray,setLikeArray]=useState(post.likes.length);

  useEffect(() => {
      const getUser = async () => {
          const obj = {
            id: post.userId
          }
          const res = await axios.post("http://localhost:9002/api/user", obj)
          setUser(res.data.res_user)
      }
      getUser()
  }, [])
  useEffect(() => {
    const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
    setLikeClick(post.likes.includes(id))
}, [])

  const handleLikeClick = () => {
      const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
      const obj = {
          postId: post._id,
          userId: id
      }
      axios.post("http://localhost:9002/api/updateLike", obj)
      if(likeClick===true){
        setLikeClick(false);
        setLikeArray((prev)=>prev-1)
      }else{
        setLikeClick(true);
        setLikeArray((prev)=>prev+1)
      }
  }

  return (
    <>
        <div className='single-post-container'>

            <div className='first'>
              <div className='profile-pic'>
                <img src={user && "http://localhost:9002/images/" + user.profilePic}></img>
              </div>
              <div className='first-username'>
                <span>{user && user.username}</span>
              </div>
              <div className='first-time'>
                <span>(time updated ago)</span>
              </div>
            </div>

            <div className='second'>
              <div>
                <span>{post.caption}</span>
              </div>
            </div>
            <div className='third'>
              <div>
                <img
                     className='post-pic'
                     src={post && "http://localhost:9002/images/" + post.postPic}
                />  
              </div>
            </div>
            <div className='fourth'>
              <div className='like-container'>
                  <FavoriteIcon style={{ color: "red" }} onClick={handleLikeClick} className='button'/>
                  <span>{likeArray} likes</span>
              </div>

              <div className='comment-container'>
                  <span>{post.comments && post.comments.length} comments</span>
              </div>
            </div>
        </div>

    </>
  )
}

export default SinglePost