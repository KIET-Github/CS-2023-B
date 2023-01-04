const mongoose = require("mongoose")
// Idea = Linkedin type follow connection request


const UserSchema=new mongoose.Schema({
    username: {
        type: String,
    },
    email:{
        type: String
    },
    password: {
        type: String,
    },
    age: {
        type: Number,
        default: 0
    },
    dateOfBirth: {
        type:String,
        default: ""
    },
    profilePic: {
        type: String,
        default: ""
    },
    coverPic:{
        type: String,
        default: ""
    },
    friends:{
        type: [String],
    },
    requests: {
        type: [String],
    }

});

module.exports = mongoose.model('User',UserSchema);
