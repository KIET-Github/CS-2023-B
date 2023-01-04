const router = require("express").Router();
const paymentController = require('../controllers/payment')

router.post("/payment",paymentController.payment);

module.exports = router;