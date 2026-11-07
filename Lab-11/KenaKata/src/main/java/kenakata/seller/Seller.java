package kenakata.seller;

import java.util.Objects;

import kenakata.util.Validate;

public final class Seller {

    private final String id;
    private final String name;

    public Seller(String id, String name) {
        this.id = Validate.notBlank(
                id,
                "Seller ID"
        );

        this.name = Validate.notBlank(
                name,
                "Seller name"
        );
    }

    public Seller(String name) {
        this(name, name);
    }

    public String getId() {
        return id;
    }

    public String getSellerId() {
        return id;
    }

    public String id() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSellerName() {
        return name;
    }

    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name + " (" + id + ")";
    }

    @Override
    public boolean equals(Object object) {

        if (this == object) {
            return true;
        }

        if (!(object instanceof Seller seller)) {
            return false;
        }

        return id.equals(seller.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

} // Seller class ends here