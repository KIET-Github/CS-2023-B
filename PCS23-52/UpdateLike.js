const router = require("express").Router();
const Post = require('../models/Post')

router.post("/", async (req, res) => {
    // res.send("My Api login")

    const {postId, userId} = req.body
    // console.log("ye meri id hai",id)

    const post = await Post.findOne({_id: postId})

    if(post.likes.includes(userId)===true){
        await post.updateOne({$pull:{likes:userId}})

    }else{
        await post.updateOne({$push:{likes:userId}})

    }
})

module.exports = router