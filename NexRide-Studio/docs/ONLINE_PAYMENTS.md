# PayHere setup: test first, then live

NexRide creates invoices in LKR. A customer may choose LKR or USD for a PayHere hosted checkout. The server snapshots the selected amount, currency and exchange rate in a checkout attempt. The customer enters card details on PayHere's page; NexRide never sees or stores card number/CVV.

## Sandbox

1. Create/configure your PayHere merchant account and add the exact integration domain as required by PayHere.
2. Set environment values before starting the app:

```text
PAYHERE_MODE=sandbox
PAYHERE_MERCHANT_ID=your_sandbox_merchant_id
PAYHERE_MERCHANT_SECRET=your_domain_specific_secret
NEXRIDE_PUBLIC_URL=https://your-public-test-host.example
```

3. Ensure the public host routes `/api/payments/payhere/notify` to this app. Payment callbacks cannot reach `localhost`.
4. In Admin → Website settings, refresh the USD rate, add your contact details and save.
5. Complete an approved rental, then use its invoice to test the hosted checkout and callback. Check both invoice and payment history after callback.

## Live

Only after the merchant account and domain are approved by PayHere, switch `PAYHERE_MODE=live`, use live merchant credentials, configure HTTPS and test an end-to-end payment/refund procedure with the merchant. Keep secrets out of source control and never put them into HTML, JavaScript, `application.properties`, or the ZIP.

PayHere currently documents support for LKR and USD checkout, Visa and Mastercard, and hosted checkout with a server notification URL. The account's enabled currencies, plan limits and settlement/payout options depend on merchant approval and plan; verify those directly with PayHere before charging customers. Currency display conversion is informational until the exchange rate is refreshed and timestamp reviewed.

## Current constraints

- Online payments stay disabled by default.
- Only LKR and USD are offered in the app.
- The USD rate can be refreshed from the admin screen or manually entered.
- USD checkout requires a positive configured exchange rate.
- A provider callback is required to mark hosted checkout paid. Returning to the website alone never marks an invoice paid.
- The admin cash action is a manual accounting record. Use it only after actually receiving the payment.
