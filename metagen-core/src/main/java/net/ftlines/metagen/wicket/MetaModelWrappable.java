/**
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package net.ftlines.metagen.wicket;

/**
 * A marker interface representing an object that is not an IModel or a wicket Component but that is
 * still appropriate for wrapping in a MetaModel
 * 
 * NOTE: THIS IS IN A WICKET PACKAGE TO MAINTAIN BACKWARDS COMPATIBILITY FROM WHEN THIS INTERFACE
 * LIVED IN metagen-wicket MODULE
 * 
 * TODO: Deprecate this and allow MetaModel to take any Object, which will remove the need for this
 * interface
 */
public interface MetaModelWrappable
{
}

