const router = require("express").Router();
const Message = require('../models/Message')

router.post("/", async (req, res) => {
    // console.log(req.body)

    const {senderId, receiverId, message} = req.body

    Message.findOne({senderId: senderId, receiverId: receiverId}, async (err, conversation) => {
        if(conversation){
            await conversation.updateOne({$push:{messageArray:{message: message, time: Date.now(), sender: senderId}}})
            res.send({message: "User Already Registered goto login page"})
        }else{
            const conversation = new Message({
                senderId: senderId,
                receiverId: receiverId,
                messageArray: [{message: message, time: Date.now(), sender: senderId}]
            })
        
            await conversation.save(err => {
                if(err){
                    res.send(err)
                }else{
                    res.send({message: "Successfully Registered"})
                }
            })
        }
    })
})

module.exports = router