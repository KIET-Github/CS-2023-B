const router = require("express").Router();
const User = require('../models/User')
const Friend = require('../models/Friends')

router.post("/1", async (req, res) => {
    
    try {

        const {userId, friendId} = req.body

        const user = await Friend.findOne({userId: userId})

        if(user){
            await user.updateOne({$addToSet:{friends:friendId}})
            res.send({message: "User Already Registered goto login page"})
        }else{
            const user = new Friend({
                userId: userId,
                friends: [friendId]
            })
        
            await user.save(err => {
                if(err){
                    res.send(err)
                }else{
                    res.send({message: "Successfully Registered"})
                }
            })
        }
        

    } catch (err) {
        console.log("nhi hua")
        console.log(err)
    }

})

router.post("/2", async (req, res) => {
    
    try {

        const {userId, friendId} = req.body
        const  user = await Friend.findOne({userId: friendId})
        if(user){
            await user.updateOne({$addToSet:{friends:userId}})
            // res.send({message: "User Already Registered goto login page"})
        }else{
            const user = new Friend({
                userId: friendId,
                friends: [userId]
            })
        
            await user.save(err => {
                if(err){
                    res.send(err)
                }else{
                    res.send({message: "Successfully Registered"})
                }
            })
        }

        
        

    } catch (err) {
        console.log("nhi hua")
        console.log(err)
    }

})

module.exports = router