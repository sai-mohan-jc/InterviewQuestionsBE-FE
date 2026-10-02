function TransactionTable() {
  const transactions = [
    {
      reference: 'TXN10001',
      type: 'Debit',
      amount: '₹5,000',
      status: 'SUCCESS'
    },
    {
      reference: 'TXN10002',
      type: 'Credit',
      amount: '₹10,000',
      status: 'SUCCESS'
    },
    {
      reference: 'TXN10003',
      type: 'Debit',
      amount: '₹2,500',
      status: 'PENDING'
    }
  ];

  return (
    <div className="transactions">

      <h2>Recent Transactions</h2>

      <table>

        <thead>
          <tr>
            <th>Reference</th>
            <th>Type</th>
            <th>Amount</th>
            <th>Status</th>
          </tr>
        </thead>

        <tbody>
          {transactions.map((transaction) => (
            <tr key={transaction.reference}>
              <td>{transaction.reference}</td>
              <td>{transaction.type}</td>
              <td>{transaction.amount}</td>
              <td>{transaction.status}</td>
            </tr>
          ))}
        </tbody>

      </table>

    </div>
  );
}

export default TransactionTable;