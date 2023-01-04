import axios from 'axios'
import React, { useEffect, useState, useRef } from 'react'
import { io } from "socket.io-client";

const ChatPage = () => {

    const [friendsArray, setFriendsArray] = useState([])    
    const [message, setMessage] = useState("")
    const [friend, setFriend] = useState()
    const [chat, setChat] = useState([])
    const socket = useRef();
    const [arrivalMessage, setArrivalMessage] = useState({
        sender: "",
        message: "",
    })

    useEffect(() => {
        socket.current = io("ws://localhost:8900");
        // socket.current.emit("addUser",id);
        
        socket.current.on("getMessage", (data) => {
        console.log("grbthbyhtbyhb",data);
            setArrivalMessage({
            sender: data.senderId,
            message: data.message,
        });
        });
    }, []);
    
    useEffect(() => {
        if(friend && arrivalMessage && friend._id === arrivalMessage.sender){
            console.log("4545454", arrivalMessage)
            const obj = {
                message: arrivalMessage.message,
                time: Date.now()
            }
            console.log("yeh mera hai", obj)
            setChat((prev) => [...prev, obj]);
        }
    }, [arrivalMessage,friend]);


    useEffect(() => {
        const getFriends = async () => {
          
          const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
          
            // socket connection 
          socket.current.emit("addUser",id);

          const obj = {id: id}
          const getFriendsArray = await axios.post("http://localhost:9002/api/getFriends", obj)
          setFriendsArray(getFriendsArray.data.friends)
        }
        getFriends()
    }, [])

    useEffect(() => {
        const getAllChat = async () => {
            const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
            if(friend){
         //       console.log(friend.username)
                const obj = {
                    senderId: id,
                    receiverId: friend._id
                }
                const res = await axios.post("http://localhost:9002/api/getAllChats", obj)
                setChat(res.data.messageArray)
            }
        }
        getAllChat()
    }, [friend])

    const handleMessageSend = async (item) =>{
        const id = JSON.parse(window.localStorage.getItem('Socially_Current_User'))._id
          
        const obj = {
            senderId: id,
            receiverId: friend._id,
            message: message
        }
        setChat((prev) => [...prev, { message: message,
            time: Date.now()
      }]);
        
        socket.current.emit("sendMessage", obj);
        await axios.post("http://localhost:9002/api/saveChatMessage", obj);
    
    }

    return (
        <div className='chat-page'>
            <div className='chat-page-left'>
                {
                    (friendsArray && friendsArray.map((item) => {
                        return (
                            <div style={{margin: "20px", cursor: "pointer"}}  onClick={() => setFriend(item)}>{item.username}</div>
                        )
                    }))
                }
            </div>
            <div className='chat-page-middle'>
                <div>
                    {
                        (chat && chat.map((item) => {
                            return (
                                <div>
                                    {console.log(item)}
                                    {item.message}
                                </div>
                            )
                        }))
                    }
                </div>
                <div>
                    <textarea onChange={(e) => setMessage(e.target.value)}></textarea>
                    <button onClick={handleMessageSend}>Send</button>
                </div>
            </div>
        </div>
    )
}

export default ChatPage