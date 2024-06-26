package com.zipcode.moneyapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import java.io.Serializable;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * Transaction entity
 */
@Schema(description = "Transaction entity")
@Entity
@Table(name = "transaction")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Transaction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "transaction_value")
    private Double transactionValue;

    @Column(name = "transaction_date")
    private Date transactionDate;

    /**
     * Associate each Transaction with a source BankAccount
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "accountHolder", "transactionsOuts", "transactionsIns" }, allowSetters = true)
    private BankAccount source;

    /**
     * Associate each Transaction with a destination BankAccount
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "accountHolder", "transactionsOuts", "transactionsIns" }, allowSetters = true)
    private BankAccount destination;

    @org.springframework.data.annotation.Transient
    private String description = null;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Transaction() {}

    public Long getId() {
        return this.id;
    }

    public Transaction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getTransactionValue() {
        return this.transactionValue;
    }

    public Transaction transactionValue(Double transactionValue) {
        this.setTransactionValue(transactionValue);
        return this;
    }

    public void setTransactionValue(Double transactionValue) {
        this.transactionValue = transactionValue;
    }

    public BankAccount getSource() {
        return this.source;
    }

    public void setSource(BankAccount bankAccount) {
        this.source = bankAccount;
    }

    public Transaction source(BankAccount bankAccount) {
        this.setSource(bankAccount);
        return this;
    }

    public BankAccount getDestination() {
        return this.destination;
    }

    public void setDestination(BankAccount bankAccount) {
        this.destination = bankAccount;
    }

    public Transaction destination(BankAccount bankAccount) {
        this.setDestination(bankAccount);
        return this;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public Transaction transactionDate(Date transactionDate) {
        try {
            this.setTransactionDate(Date.valueOf(transactionDate.toString()));
        } catch (NullPointerException e) {
            this.transactionDate = null;
        }
        return this;
    }

    public void setTransactionDate(Date transactionDate) {
        try {
            this.transactionDate = Date.valueOf(transactionDate.toString());
        } catch (NullPointerException e) {
            this.transactionDate = null;
        }
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Transaction)) {
            return false;
        }
        return getId() != null && getId().equals(((Transaction) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Transaction{" +
            "id=" + getId() +
            ", transactionValue=" + getTransactionValue() +
            "}";
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Transaction description(String description) {
        this.setDescription(description);
        return this;
    }

    public String generateDescription() {
        if (source == null && destination == null) {
            System.out.println("both null");
            this.description = null;
        } else if (source == null && destination != null) {
            System.out.println("source null, dest nonnull");
            this.description = "Deposit into " + this.destination.getType().toString();
        } else if (source != null && destination == null) {
            System.out.println("source nonnull, dest null");
            this.description = "Withdrawal from " + this.source.getType().toString();
        } else {
            System.out.println("both nonnull");
            System.out.println(this.source);
            System.out.println(this.destination);
            this.description = "Transfer from " + this.source.getType().toString() + " to " + this.destination.getType().toString();
        }
        return description;
    }
}
