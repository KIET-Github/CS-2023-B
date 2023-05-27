const router = require("express").Router();
const Message = require('../models/Message')

router.post("/", async (req, res) => {
    // console.log(req.body)

    try {
        const {senderId, receiverId} = req.body

        const conversation1 = await Message.findOne({senderId: senderId, receiverId: receiverId})
        const conversation2 = await Message.findOne({senderId: receiverId, receiverId: senderId})

        if(conversation1 && conversation2){
            res.send({message: "all chats fetched successfully", messageArray: [...conversation1.messageArray, ...conversation2.messageArray]})

        }

        else if(conversation1){
            console.log(conversation1.messageArray)
            res.send({message: "all chats fetched successfully", messageArray: conversation1.messageArray})
        }else if(conversation2) {
            console.log(conversation2.messageArray)
            res.send({message: "all chats fetched successfully", messageArray: conversation2.messageArray})
        }else{
            res.send({message: "no  chats are fetched ", messageArray: []})
        }

    } catch(err) {
        console.log(err)
    }
})

module.exports = router