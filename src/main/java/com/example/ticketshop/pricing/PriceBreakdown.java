package com.example.ticketshop.pricing;

import java.math.BigDecimal;

public record PriceBreakdown(BigDecimal subtotal, BigDecimal bulkDiscount, BigDecimal serviceFee, BigDecimal total) {
}
