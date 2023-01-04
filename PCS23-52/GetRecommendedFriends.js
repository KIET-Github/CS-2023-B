const router = require("express").Router();
const User = require('../models/User')

router.post("/", async (req, res) => {

    const {userId} = req.body
    const user = await User.findOne({_id: userId})

    var recommendedFriendList = []
    const map = new Map();

    map.set(userId, 1);


    var queue = []
    for(let i=0;i<user.friends.length;i++) {
        queue.push(user.friends[i])
        map.set(user.friends[i], 1)
    }

    var flag = false

    while(queue.length > 0) {
        let size = queue.length

        for(let i=0;i<size;i++) {
            const friendId = queue.shift()

            const friendUser = await User.findOne({_id: friendId})

            for(let j=0;j<friendUser.friends.length;j++) {
                if(!map.has(friendUser.friends[j])){
                    queue.push(friendUser.friends[j])
                    map.set(friendUser.friends[j], 1)
                    recommendedFriendList.push(friendUser.friends[j])

                    if(recommendedFriendList.length > 4){
                        flag = true
                        break
                    }
                }
            }
            if(flag) break
        }
        if(flag) break
    }

    var recommendedFriendListArray = []
    
    for(let i=0;i<recommendedFriendList.length;i++){
        const friendUser = await User.findOne({_id: recommendedFriendList[i]})
        recommendedFriendListArray.push(friendUser)
    }

    res.send({message: "Recommended friends fetched successfully", recommendedFriendListArray})
    console.log(recommendedFriendList)

})

module.exports = router