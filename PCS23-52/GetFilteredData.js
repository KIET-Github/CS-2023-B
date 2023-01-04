const router = require("express").Router();
const User = require('../models/User')

router.post("/", (req, res) => {
    // res.send("My Api login")

    const {query} = req.body
    // console.log(query)
    // User.findOne({_id: id}, (err, user) => {
    //     res.send({message: "get user Successfully", res_user: user})
    //     console.log(user)
    // })

    User.find({username : {$regex : new RegExp('^' + query, 'i')}}, (err, user_array) => {
        res.send({message: "get user filtered data Successfully", res_user: user_array})
        // console.log(user_array)
    })


})

module.exports = router