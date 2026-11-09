import { useState } from 'react';
import './AIAssistant.css';

function AIAssistant() {
  const [question, setQuestion] = useState('');
  const [messages, setMessages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const sendMessage = async (event) => {
    event.preventDefault();

    const userQuestion = question.trim();

    if (!userQuestion || loading) {
      return;
    }

    setQuestion('');
    setError('');
    setMessages((previous) => [
      ...previous,
      { role: 'user', content: userQuestion }
    ]);

    setLoading(true);

    const controller = new AbortController();
    const timeoutId = setTimeout(() => controller.abort(), 35000);

    try {
      const response = await fetch('/api/ai/chat', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify({ question: userQuestion }),
        signal: controller.signal
      });

      const answer = await response.text();

      if (!response.ok) {
        throw new Error(
          answer || `AI request failed with status ${response.status}`
        );
      }

      setMessages((previous) => [
        ...previous,
        { role: 'assistant', content: answer }
      ]);
    } catch (err) {
      if (err.name === 'AbortError') {
        setError('The AI response is taking too long. Please try again.');
      } else {
        setError(err.message || 'Unable to connect to AI Assistant.');
      }
    } finally {
      clearTimeout(timeoutId);
      setLoading(false);
    }
  };

  return (
    <div className="content ai-page">
      <div className="page-header">
        <div>
          <h2>AI Banking Assistant</h2>
          <p>Ask questions and get assistance from our AI service.</p>
        </div>
      </div>

      <div className="ai-chat-container">
        <div className="ai-chat-messages">
          {messages.length === 0 && (
            <div className="ai-welcome">
              <h3>Hello! How can I help you?</h3>
              <p>
                You can ask questions about banking, KYC, transactions
                and other banking concepts.
              </p>
            </div>
          )}

          {messages.map((message, index) => (
            <div
              key={index}
              className={`ai-message ${message.role}`}
            >
              <strong>
                {message.role === 'user' ? 'You' : 'AI Assistant'}
              </strong>
              <p>{message.content}</p>
            </div>
          ))}

          {loading && (
            <div className="ai-loading">
              AI Assistant is preparing your response...
            </div>
          )}

          {error && (
            <div className="ai-error">
              {error}
            </div>
          )}
        </div>

        <form className="ai-chat-form" onSubmit={sendMessage}>
          <input
            type="text"
            value={question}
            onChange={(event) => setQuestion(event.target.value)}
            placeholder="Ask your banking question..."
            disabled={loading}
          />

          <button type="submit" disabled={loading || !question.trim()}>
            {loading ? 'Sending...' : 'Submit'}
          </button>
        </form>
      </div>
    </div>
  );
}

export default AIAssistant;