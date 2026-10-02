import { Link } from 'react-router-dom';

function Sidebar() {
  return (
    <aside className="sidebar">

      <h3>Menu</h3>

      <ul>

        <li>
          <Link to="/">Dashboard</Link>
        </li>

        <li>
          <Link to="/profile">Profile</Link>
        </li>

        <li>
          <Link to="/accounts">Accounts</Link>
        </li>

        <li>
          <Link to="/transfers">Transfers</Link>
        </li>


        <li>
          <Link to="/transactions">Transactions</Link>
        </li>

        
      </ul>

    </aside>
  );
}

export default Sidebar;