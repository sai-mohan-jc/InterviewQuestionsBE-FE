function Profile() {
  const customer = {
    cif: 'CIF10001',
    name: 'John Doe',
    email: 'john.doe@example.com',
    mobile: '9876543210',
    kycStatus: 'VERIFIED',
    status: 'ACTIVE'
  };

  return (
    <div>
      <h2>My Profile</h2>

      <div className="card">
        <p><strong>CIF:</strong> {customer.cif}</p>
        <p><strong>Name:</strong> {customer.name}</p>
        <p><strong>Email:</strong> {customer.email}</p>
        <p><strong>Mobile:</strong> {customer.mobile}</p>
        <p><strong>KYC Status:</strong> {customer.kycStatus}</p>
        <p><strong>Status:</strong> {customer.status}</p>
      </div>
    </div>
  );
}

export default Profile;