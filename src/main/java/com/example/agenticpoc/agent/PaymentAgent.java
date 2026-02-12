package com.example.agenticpoc.agent;

import com.example.agenticpoc.model.AgentRequest;
import com.example.agenticpoc.model.AgentResponse;
import com.example.agenticpoc.model.PaymentSummary;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentAgent implements AgentHandler
{
    private static final BigDecimal DEFAULT_PENDING_AMOUNT = new BigDecimal( "25.00" );

    @Override
    public String getAgentType()
    {
        return "payment";
    }

    @Override
    public AgentResponse handle( AgentRequest request )
    {
        PaymentSummary paymentSummary = checkPendingPayments( request );
        Map<String, String> data = new LinkedHashMap<>();

        if ( StringUtils.hasText( paymentSummary.billingAccountId() ) )
        {
            data.put( "billingAccountId", paymentSummary.billingAccountId() );
        }

        data.put( "pendingPayments", Boolean.toString( paymentSummary.hasPendingPayments() ) );
        data.put( "pendingAmount", paymentSummary.pendingAmount().toPlainString() );
        if ( StringUtils.hasText( paymentSummary.reason() ) )
        {
            data.put( "pendingReason", paymentSummary.reason() );
        }

        String summary = paymentSummary.hasPendingPayments()
            ? "PaymentAgent found pending payments"
            : "PaymentAgent found no pending payments";

        if ( request != null && StringUtils.hasText( request.prompt() ) )
        {
            summary = summary + " for prompt: " + request.prompt();
        }

        return new AgentResponse( "PaymentAgent", summary, data, List.of() );
    }

    public PaymentSummary checkPendingPayments( AgentRequest request )
    {
        String billingAccountId = request == null ? null : request.billingAccountId();
        String memberId = request == null ? null : request.memberId();
        boolean hasPendingPayments = endsWithDigit( billingAccountId, '9' ) || endsWithDigit( memberId, '9' );
        BigDecimal pendingAmount = hasPendingPayments ? DEFAULT_PENDING_AMOUNT : BigDecimal.ZERO;
        String reason = hasPendingPayments ? "Pending dues detected" : "No pending payments detected";

        return new PaymentSummary( billingAccountId, hasPendingPayments, pendingAmount, reason );
    }

    private boolean endsWithDigit( String value, char digit )
    {
        if ( !StringUtils.hasText( value ) )
        {
            return false;
        }

        return value.trim().endsWith( String.valueOf( digit ) );
    }
}
