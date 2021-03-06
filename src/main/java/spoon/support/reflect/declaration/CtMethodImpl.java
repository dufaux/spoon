/*
 * Spoon - http://spoon.gforge.inria.fr/
 * Copyright (C) 2006 INRIA Futurs <renaud.pawlak@inria.fr>
 *
 * This software is governed by the CeCILL-C License under French law and
 * abiding by the rules of distribution of free software. You can use, modify
 * and/or redistribute the software under the terms of the CeCILL-C license as
 * circulated by CEA, CNRS and INRIA at http://www.cecill.info.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the CeCILL-C License for more details.
 *
 * The fact that you are presently reading this means that you have had
 * knowledge of the CeCILL-C license and that you accept its terms.
 */

package spoon.support.reflect.declaration;

import spoon.reflect.declaration.CtElement;
import spoon.reflect.declaration.CtGenericElement;
import spoon.reflect.declaration.CtMethod;
import spoon.reflect.declaration.CtModifiable;
import spoon.reflect.declaration.CtType;
import spoon.reflect.declaration.CtTypedElement;
import spoon.reflect.declaration.ModifierKind;
import spoon.reflect.reference.CtTypeReference;
import spoon.reflect.visitor.CtVisitor;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static spoon.reflect.ModelElementContainerDefaultCapacities.METHOD_TYPE_PARAMETERS_CONTAINER_DEFAULT_CAPACITY;

/**
 * The implementation for {@link spoon.reflect.declaration.CtMethod}.
 *
 * @author Renaud Pawlak
 */
public class CtMethodImpl<T> extends CtExecutableImpl<T> implements CtMethod<T> {
	private static final long serialVersionUID = 1L;

	CtTypeReference<T> returnType;

	boolean defaultMethod = false;

	List<CtTypeReference<?>> formalTypeParameters = emptyList();

	Set<ModifierKind> modifiers = CtElementImpl.emptySet();

	public CtMethodImpl() {
		super();
	}

	@Override
	public void accept(CtVisitor v) {
		v.visitCtMethod(this);
	}

	@Override
	public CtTypeReference<T> getType() {
		return returnType;
	}

	@Override
	public <C extends CtTypedElement> C setType(CtTypeReference<T> type) {
		if (type != null) {
			type.setParent(this);
		}
		this.returnType = type;
		return (C) this;
	}

	@Override
	public CtType<?> getDeclaringType() {
		return (CtType<?>) parent;
	}

	@Override
	public boolean isDefaultMethod() {
		return defaultMethod;
	}

	@Override
	public <C extends CtMethod<T>> C setDefaultMethod(boolean defaultMethod) {
		this.defaultMethod = defaultMethod;
		return (C) this;
	}

	@Override
	public List<CtTypeReference<?>> getFormalTypeParameters() {
		return formalTypeParameters;
	}

	@Override
	public <T extends CtGenericElement> T addFormalTypeParameter(CtTypeReference<?> formalTypeParameter) {
		if (formalTypeParameter == null) {
			return (T) this;
		}
		if (formalTypeParameters == CtElementImpl.<CtTypeReference<?>>emptyList()) {
			formalTypeParameters = new ArrayList<CtTypeReference<?>>(
					METHOD_TYPE_PARAMETERS_CONTAINER_DEFAULT_CAPACITY);
		}
		formalTypeParameter.setParent(this);
		formalTypeParameters.add(formalTypeParameter);
		return (T) this;
	}

	@Override
	public <T extends CtGenericElement> T setFormalTypeParameters(List<CtTypeReference<?>> formalTypeParameters) {
		if (this.formalTypeParameters == CtElementImpl.<CtTypeReference<?>>emptyList()) {
			this.formalTypeParameters = new ArrayList<CtTypeReference<?>>(
					METHOD_TYPE_PARAMETERS_CONTAINER_DEFAULT_CAPACITY);
		}
		this.formalTypeParameters.clear();
		for (CtTypeReference<?> formalTypeParameter : formalTypeParameters) {
			addFormalTypeParameter(formalTypeParameter);
		}
		return (T) this;
	}

	@Override
	public boolean removeFormalTypeParameter(CtTypeReference<?> formalTypeParameter) {
		return formalTypeParameter != null
				&& formalTypeParameters != CtElementImpl.<CtTypeReference<?>>emptyList()
				&& formalTypeParameters.remove(formalTypeParameter);
	}

	@Override
	public Set<ModifierKind> getModifiers() {
		return modifiers;
	}

	@Override
	public boolean hasModifier(ModifierKind modifier) {
		return getModifiers().contains(modifier);
	}

	@Override
	public <C extends CtModifiable> C setModifiers(Set<ModifierKind> modifiers) {
		this.modifiers = modifiers;
		return (C) this;
	}

	@Override
	public <C extends CtModifiable> C addModifier(ModifierKind modifier) {
		if (modifiers == CtElementImpl.<ModifierKind>emptySet()) {
			this.modifiers = EnumSet.noneOf(ModifierKind.class);
		}
		modifiers.add(modifier);
		return (C) this;
	}

	@Override
	public boolean removeModifier(ModifierKind modifier) {
		return !modifiers.isEmpty() && modifiers.remove(modifier);
	}

	@Override
	public <C extends CtModifiable> C setVisibility(ModifierKind visibility) {
		if (modifiers == CtElementImpl.<ModifierKind>emptySet()) {
			this.modifiers = EnumSet.noneOf(ModifierKind.class);
		}
		getModifiers().remove(ModifierKind.PUBLIC);
		getModifiers().remove(ModifierKind.PROTECTED);
		getModifiers().remove(ModifierKind.PRIVATE);
		getModifiers().add(visibility);
		return (C) this;
	}

	@Override
	public ModifierKind getVisibility() {
		if (getModifiers().contains(ModifierKind.PUBLIC)) {
			return ModifierKind.PUBLIC;
		}
		if (getModifiers().contains(ModifierKind.PROTECTED)) {
			return ModifierKind.PROTECTED;
		}
		if (getModifiers().contains(ModifierKind.PRIVATE)) {
			return ModifierKind.PRIVATE;
		}
		return null;
	}

	@Override
	public <R extends T> void replace(CtMethod<T> element) {
		replace((CtElement) element);
	}
}
