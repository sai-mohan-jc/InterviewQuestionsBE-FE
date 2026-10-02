import { useState } from 'react';

function Transfer() {
  const [transfer, setTransfer] = useState({
    fromAccount: 'Savings Account - XXXX4521',
    beneficiary: 'Ravi Kumar - XXXX7890',
    amount: '',
    method: 'IMPS',
    remarks: ''
  });

  const handleChange = (event) => {
    setTransfer({
      ...transfer,
      [event.target.name]: event.target.value
    });
  };

  const handleSubmit = (event) => {
    event.preventDefault();

    console.log('Transfer Request:', transfer);

    alert(`Transfer of ₹${transfer.amount} initiated`);
  };

  return (
    <div>
      <h2>Transfer Money</h2>

      <div className="card">
        <form onSubmit={handleSubmit}>

          <label>From Account</label>
          <select
            name="fromAccount"
            value={transfer.fromAccount}
            onChange={handleChange}
          >
            <option>Savings Account - XXXX4521</option>
            <option>Current Account - XXXX7812</option>
          </select>

          <label>Beneficiary</label>
          <select
            name="beneficiary"
            value={transfer.beneficiary}
            onChange={handleChange}
          >
            <option>Ravi Kumar - XXXX7890</option>
            <option>Priya Sharma - XXXX1234</option>
          </select>

          <label>Amount</label>
          <input
            type="number"
            name="amount"
            placeholder="Enter amount"
            value={transfer.amount}
            onChange={handleChange}
          />

          <label>Transfer Method</label>

          <div>
            <label>
              <input
                type="radio"
                name="method"
                value="IMPS"
                checked={transfer.method === 'IMPS'}
                onChange={handleChange}
              />
              IMPS
            </label>

            <label>
              <input
                type="radio"
                name="method"
                value="NEFT"
                checked={transfer.method === 'NEFT'}
                onChange={handleChange}
              />
              NEFT
            </label>

            <label>
              <input
                type="radio"
                name="method"
                value="RTGS"
                checked={transfer.method === 'RTGS'}
                onChange={handleChange}
              />
              RTGS
            </label>
          </div>

          <label>Remarks</label>

          <input
            type="text"
            name="remarks"
            placeholder="Enter remarks"
            value={transfer.remarks}
            onChange={handleChange}
          />

          <button type="submit">
            Transfer ₹{transfer.amount || '0'}
          </button>

        </form>
      </div>
    </div>
  );
}

export default Transfer;