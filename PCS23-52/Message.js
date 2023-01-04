const mongoose = require("mongoose")

const MessageSchema = new mongoose.Schema(
  {
    senderId: {
      type: String,
      default: ""
    },
    receiverId: {
        type: String,
        default: ""
    },
    messageArray: {
      type: Array,
      default: [
        {
            message: {
                type: String,
                default: ""
            },
            time: { 
                type: Number,
                default: 0 
            },
            sender: {
                type: String,
                default: ""
            }
        }
      ]
    },
  }
);

module.exports = mongoose.model("Message", MessageSchema);