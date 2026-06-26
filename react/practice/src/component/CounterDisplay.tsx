type CounterDisplayProps = {
  count: number;
};

function CounterDisplay({
  count,
}: CounterDisplayProps) {
  return <h2>Current Count: {count}</h2>;
}

export default CounterDisplay;