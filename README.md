# Agentic AI POC (Spring Boot)

This is a minimal Spring Boot project that demonstrates a master agent routing
requests to domain agents, with inter-agent communication.

Flow covered:
- MasterAgent routes to CancelAgent when the prompt contains "cancel".
- CancelAgent calls PaymentAgent to check for pending payments.

## Prerequisites
- Java 17+
- Maven 3.9+

## Run the application

```bash
mvn spring-boot:run
```

The app will start on `http://localhost:8080`.

## Example request (cancel membership)

```bash
curl -X POST http://localhost:8080/agentic/master \
  -H "Content-Type: application/json" \
  -d '{
        "prompt": "I want to cancel my membership.",
        "memberId": "M12349",
        "billingAccountId": "B88888"
      }'
```

## Direct endpoints

- `POST /agentic/master`
- `POST /agentic/cancel`
- `POST /agentic/payment`

## Notes

- Pending payment check is deterministic for demo purposes:
  - Any `memberId` or `billingAccountId` ending with `9` is treated as pending.
- You do NOT need LM Studio or any LLM runtime for this demo. The agents are
  deterministic and do not call an LLM. If you want a real LLM, you can later
  plug in OpenAI, Azure OpenAI, Ollama, or LM Studio.
