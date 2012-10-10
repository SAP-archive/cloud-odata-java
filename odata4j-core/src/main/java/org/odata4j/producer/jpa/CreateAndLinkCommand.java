package org.odata4j.producer.jpa;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.OneToMany;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.PluralAttribute;

import org.core4j.Enumerable;
import org.core4j.Predicate1;
import org.odata4j.edm.EdmMultiplicity;
import org.odata4j.edm.EdmNavigationProperty;

public class CreateAndLinkCommand implements Command {

  @Override
  public boolean execute(JPAContext context) {
    // get the navigation property
    EdmNavigationProperty edmNavProperty = context.getEntity()
        .getEdmEntitySet().getType()
        .findNavigationProperty(context.getNavProperty());

    // check whether the navProperty is valid
    if (edmNavProperty == null
        || edmNavProperty.getToRole().getMultiplicity() != EdmMultiplicity.MANY) {
      throw new IllegalArgumentException(
          "unknown navigation property "
              + context.getNavProperty()
              + " or navigation property toRole Multiplicity is not '*'");
    }

    EntityType<?> newJpaEntityType = context.getOtherEntity()
        .getJPAEntityType();
    Object newJpaEntity = context.getOtherEntity().getJpaEntity();

    // get the collection attribute and add the new entity to the parent
    // entity
    final String navProperty = context.getNavProperty();
    @SuppressWarnings({ "unchecked", "rawtypes" })
    PluralAttribute<?, ?, ?> attr = Enumerable.create(
        context.getEntity().getJPAEntityType()
            .getPluralAttributes())
        .firstOrNull(new Predicate1() {
          public boolean apply(Object input) {
            PluralAttribute<?, ?, ?> pa = (PluralAttribute<?, ?, ?>) input;
            return pa.getName().equals(navProperty);
          }
        });
    JPAMember member = JPAMember.create(attr, context.getEntity()
        .getJpaEntity());
    Collection<Object> collection = member.get();
    collection.add(newJpaEntity);

    // TODO handle ManyToMany relationships
    // set the backreference in bidirectional relationships
    OneToMany oneToMany = member.getAnnotation(OneToMany.class);
    if (oneToMany != null
        && oneToMany.mappedBy() != null
        && !oneToMany.mappedBy().isEmpty()) {
      JPAMember.create(
          newJpaEntityType.getAttribute(oneToMany.mappedBy()),
          newJpaEntity)
          .set(context.getEntity().getJpaEntity());
    }

    // check whether the EntityManager will persist the
    // new entity or should we do it
    if (oneToMany != null
        && oneToMany.cascade() != null) {
      List<CascadeType> cascadeTypes = Arrays.asList(oneToMany
          .cascade());
      if (!cascadeTypes.contains(CascadeType.ALL)
          && !cascadeTypes.contains(CascadeType.PERSIST)) {
        context.getEntityManager().persist(newJpaEntity);
      }
    }

    context.setResult(JPAResults
        .entity(context.getOtherEntity().getJpaEntity()));

    return false;
  }
}