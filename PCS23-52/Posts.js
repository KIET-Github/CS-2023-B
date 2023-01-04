import axios from 'axios'
import React, { useEffect, useState, useContext } from 'react'
import { Auth } from '../context/AuthContext'
import '../styles/posts.css'
import SinglePost from './SinglePost'

const Posts = ({isProfile}) => {

  const [postsArray, setPostsArray] = useState([])
  const [myPostsArray, setMyPostsArray] = useState([])

  useEffect(() => {
    const fetchPosts = async () => {
      const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
      const obj = {id: id}
      const tempArray = await axios.post("http://localhost:9002/api/getAllPosts", obj)
      setPostsArray(tempArray.data.postsArray)
      setMyPostsArray(tempArray.data.myPostsArray)
    }
    fetchPosts()
  }, [])

  return (
    <>
      {
        (isProfile)
        ?
        <div className='posts'>
          <div className='single-post'>
          {
            (myPostsArray && myPostsArray.map((item) => {
              return (
                <SinglePost post={item} key={item._id}/>
              )
            }))
          }
          </div>
        </div>
        :
        <div className='posts'>
          <div className='single-post'>
            {
              (postsArray && postsArray.map((item) => {
                return (
                  <SinglePost post={item} key={item._id}/>
                )
              }))
            }
          </div>
        </div>
      }
    </>
  )
}

export default Posts