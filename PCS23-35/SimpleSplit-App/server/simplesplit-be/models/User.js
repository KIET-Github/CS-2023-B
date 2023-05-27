const mongoose = require("mongoose");
const bcrypt = require("bcrypt");

const userSchema = mongoose.Schema(
  {
    name: {
      type: String,
      required: true,
    },
    email: {
      type: String,
      required: true,
    },
    password: {
      type: String,
      required: true,
    },
    phone: {
      type: String,
      required: true,
    },
    transaction: [
      {
        to: {
          type: String,
        },
        spent: {
          type: Number,
        },
        saved: {
          type: Number,
        },
      },
    ],
  },
  { timestamps: true }
);

const user = mongoose.model("User", userSchema);
module.exports = user;
