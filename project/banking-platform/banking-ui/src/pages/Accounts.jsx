function Accounts() {
  const accounts = [
    {
      type: 'Savings Account',
      number: 'XXXX4521',
      balance: '₹75,000'
    },
    {
      type: 'Current Account',
      number: 'XXXX7812',
      balance: '₹2,50,000'
    }
  ];

  return (
    <div>
      <h2>My Accounts</h2>

      <div className="cards">
        {accounts.map((account) => (
          <div className="card" key={account.number}>
            <h3>{account.type}</h3>
            <p>{account.number}</p>
            <h2>{account.balance}</h2>
          </div>
        ))}
      </div>
    </div>
  );
}

export default Accounts;