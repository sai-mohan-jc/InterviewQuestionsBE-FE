type CounterButtonsProps = {
  count: number;
  setCount: React.Dispatch<
    React.SetStateAction<number>
  >;
};

function CounterButtons({
  count,
  setCount,
}: CounterButtonsProps) {
  return (
    <>
      <button
        onClick={() => setCount(count + 1)}
      >
        Increment
      </button>

      <button
        onClick={() => setCount(count - 1)}
      >
        Decrement
      </button>
    </>
  );
}

export default CounterButtons;