const mongoose = require("mongoose")
// Idea = Linkedin type follow connection request


const PostSchema = new mongoose.Schema({
    userId: {
        type: String,
        default: ""
    },
    caption: {
        type: String,
        default: ""
    },
    profilePic: {
        type: String,
        default: ""
    },
    postPic: {
        type: String,
        default: ""
    },
    likes: {
        type: Array,
        default: []
    },
    comments: {
        type: [Object]
    },
    current_time: {
        type: String,
        default: Date.now()
    }
});

module.exports = mongoose.model('Post', PostSchema);
