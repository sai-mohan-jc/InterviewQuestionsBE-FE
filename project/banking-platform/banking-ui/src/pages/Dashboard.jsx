import BalanceCard from "../components/BalanceCard";
import TransactionTable from "../components/TransactionTable";

function Dashboard(){
    return (
<main className="content">
<h2>Dashboard</h2>
<div className="cards">
<BalanceCard title="" value="₹85,000"/>
<BalanceCard
          title="Account Type"
          value="Savings Account"
        />

        <BalanceCard
          title="Account Number"
          value="XXXX XXXX 4521"
        />

</div>
<TransactionTable/>
</main>

    );
}

export default Dashboard;