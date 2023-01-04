const router = require("express").Router();
const Post = require('../models/Post')
const User = require('../models/User')

router.post("/", async (req, res) => {

    const user = await User.findOne({_id: req.body.id})
    const myPostsArray = await Post.find({userId:req.body.id})

    myPostsArray.reverse()

    let allPosts = myPostsArray

    for(var i=0;i<user.friends.length;i++){
        const friendId = user.friends[i]
        const OneFriendPostArray = await Post.find({userId: friendId})
        allPosts = [...allPosts, ...OneFriendPostArray]
    }

    res.send({message: "request users fetched success", postsArray: allPosts, myPostsArray: myPostsArray})

})

module.exports = router