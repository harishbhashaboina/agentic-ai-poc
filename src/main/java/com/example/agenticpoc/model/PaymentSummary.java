package com.example.agenticpoc.model;

import java.math.BigDecimal;

public record PaymentSummary(
    String billingAccountId,
    boolean hasPendingPayments,
    BigDecimal pendingAmount,
    String reason
)
{
}
