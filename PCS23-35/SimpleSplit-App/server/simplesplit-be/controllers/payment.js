const stripe = require("stripe")(process.env.STRIPE_SECRET_KEY);
const { v4: uuidv4 } = require("uuid");

exports.payment = (req, res, next) => {
  const { price, token } = req.body;
  const idempotencyKey = uuidv4(); // So ensure that user dont pay twice for the same thing.

  return stripe.customers
    .create({
      // creating a new customer
      email: token.email,
      source: token.id,
    })
    .then((customer) => {
      // charging customer for payment
      stripe.charges.create(
        {
          amount: price * 100,
          currency: "usd",
          customer: customer.id,
          receipt_email: token.email,
        },
        idempotencyKey
      );
    })
    .then((result) => {
      res.json(200).json(result);
    })
    .catch((err) => {
      console.log(err);
      res.json(500).json("Something Went Wrong");
    });
};
